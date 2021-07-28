var ss = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1jQwMlnEuTFV7VBco0SxH-hM6hbsIJbEDgkQ_gz6JrF4/edit?pli=1#gid=2010401578");

function doPost(e){
var action = e.parameter.action;
console.log("action" , action);
if(action == 'add_item'){
  return addItem(e);
}
}

function addItem(e){

var date =  e.parameter.date;
var sheet = ss.getSheetByName('July 2021+'); 

var Date=e.parameter.date;
var Description = e.parameter.description;
var Amount = e.parameter.amount;
var CreditedOrDebited=e.parameter.credited;
var Type=e.parameter.type;
var Mode=e.parameter.modeOfPayment;
var values;

if(CreditedOrDebited=="credited"){
 values = [Date,Description,"",Amount,Type,Mode];  
}
else{
  values = [Date,Description,Amount,"",Type,Mode];
}

sheet.appendRow(values);

return ContentService.createTextOutput("Success").setMimeType(ContentService.MimeType.TEXT);

}
