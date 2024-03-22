import os
import pandas as pd
import streamlit as st
import getpass
import logging
import uvicorn

from fastapi import FastAPI, Request
from twilio.rest import Client
from langchain.prompts import PromptTemplate
from pandasql import sqldf
from langchain_nvidia_ai_endpoints import ChatNVIDIA
from langchain_core.output_parsers import StrOutputParser
from urllib.parse import parse_qs

"""
TODO:
1) User is forced to write prompts that will generate a successful SQL query.
2) SQLDF is a bottleneck it cannot run complex SQL queries
3) Although mistral model converts SQL table to plain english, it is not conversation like

Solution:
Finetune two models one to generate complex sql queries without forcing the user to worry about the nuances of SQL,
second model that can take an SQL result and convert it into plain readable english in conversation format.

Integrate the finetuned model into our langchain

Move all CSV data to an actually SQL server.
"""

request_template = """
You are a powerfule text-to-SQL model. Your job is to answer questions about a database. You are given a question and context
regarding one or more tables. Don't add \n characters.

You must output the SQL query that answers the question in single line.

### Input:
`{input}`

### Context:
`{context}`

### Response:
"""

response_template = """
You are a powerful SQL table to english model. Your job is to rephrase a set of rows from a table into plain english for a human to understand.
You are given a SQL table result and database schema to respond. Do not add "\n" characters. Don't provide the SQL query.
All amounts are in INR.

You must output the SQL query that answers the question in single line.

### Input:
{input}

### Context:
{context}

### Response:
"""

os.environ["NVIDIA_API_KEY"] = "xxxxxxxxxxxxxxx"
request_prompt = PromptTemplate.from_template(template=request_template)
response_prompt = PromptTemplate.from_template(template=response_template)
data_path = "/home/azureuser/expenses-rag-app/data/expenses_apr_2023.csv"
llm = ChatNVIDIA(model="mixtral_8x7b")
app = FastAPI()
TWILIO_ACCOUNT_SID="XXXXXXXXXXXXX"
TWILIO_AUTH_TOKEN="XXXXXXXXXXXXX"
client = Client(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN)
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

def get_nvidia_mistral_response(prompt, input, context):
    chain = prompt | llm | StrOutputParser()
    response = chain.invoke({"input": input, "context": context})
    return response

def run_streamlit_app():
    st.set_page_config(page_title="Expenses querier", layout="wide")
    st.title("Expenses chatbot")
    df = pd.read_csv(data_path, encoding="latin1")
    df.columns = df.columns.str.replace(r"[^a-zA-Z0-9_]", "", regex=True)
    context = pd.io.sql.get_schema(df.reset_index(), "df").replace('"',"")
    col1, col2 = st.columns([2,3])
    with col1:
        st.write("Here is a preview of the expenses sheet")
        st.dataframe(df)
        st.write("SQL Schema:")
        st.code(context)
    
    with col2:
        question = st.text_input("Ask a query about the data", key="question")
        if st.button("Get Answer", key="get_answer"):
            if question:
                attempt = 0
                max_attempts = 5
                while attempt < max_attempts:
                    try:
                        response = get_nvidia_mistral_response(request_prompt, question, context)
                        final = response.replace("`", "").strip()
                        result = sqldf(final, locals())
                        st.write(f"Query: {final}")
                        st.write("Answer: ")
                        st.dataframe(result)
                        break
                    except Exception as e:
                        attempt += 1
                        st.error(f"Attempt {attempt}/{max_attempts} failed. Retrying ...")
                        if attempt == max_attempts:
                            st.error("Unable to get the correct query, refresh app or try again later.")
                        continue
            else:
                st.warning("Please enter a question before clicking get answer")

def send_message(from_number, to_number, body_text):
    try:
        message = client.messages.create(
            from_ = f"whatsapp:{from_number}",
            body = body_text,
            to=f"whatsapp:{to_number}"
        )
        logger.info(f"Message sent to {to_number}: {message.body}")
    except Exception as e:
        logger.error(f"Error sending message to {to_number}: {e}")

@app.post("/")
async def reply(question: Request):
    """
    Example request:
    b'NumMedia': [b'0'], b'ProfileName': [b'Vineeth'], 
    b'MessageType': [b'text'], 
    b'WaId': [b'14156192723'], 
    b'SmsStatus': [b'received'], b'Body': [b'hey'], 
    b'To': [b'whatsapp:+14155238886'], 
    b'NumSegments': [b'1'], b'ReferralNumMedia': [b'0'], 
    b'From': [b'whatsapp:+14156192723'], b'ApiVersion': [b'2010-04-01']}
    """
    parsed = parse_qs(await question.body())
    from_number = parsed[b'From'][0].decode('utf-8').split(":")[1]
    to_number = parsed[b'To'][0].decode('utf-8').split(":")[1]
    # profile_name = parsed[b'ProfileName'][0].decode('utf-8')
    llm_question = parsed[b'Body'][0].decode('utf-8')
    df = pd.read_csv(data_path, encoding="latin1")
    df.columns = df.columns.str.replace(r"[^a-zA-Z0-9_]", "", regex=True)
    context = pd.io.sql.get_schema(df.reset_index(), "df").replace('"',"")
    try:
        print(llm_question, context)
        sql_query = get_nvidia_mistral_response(request_prompt, llm_question, context)
        final = sql_query.replace("`", "").strip()
        print(final)
        # SQLDF library is a bottleneck here. Ideally it should be saved in an SQL server
        result_df = sqldf(final, locals())
        result_string = result_df.to_string()
        print(result_string)
        plain_english_response = get_nvidia_mistral_response(response_prompt, result_string, context)
        print(plain_english_response)
        send_message(to_number, from_number, plain_english_response)
    except:
        send_message(to_number, from_number, "Sorry complex prompt")
    return result_string

if __name__ == "__main__":
    # run_streamlit_app()
    uvicorn.run(app, host="0.0.0.0", port=8080)