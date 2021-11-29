var ss = SpreadsheetApp.openByUrl("https://docs.google.com/spreadsheets/d/1jQwMlnEuTFV7VBco0SxH-hM6hbsIJbEDgkQ_gz6JrF4/edit?pli=1#gid=757043985");
var sheet = ss.getSheetByName('December 2021'); 

function doPost(e){
var action = e.parameter.action;
console.log("action" , action);
if(action == 'add_item'){
  return addItem(e);
}
}
function doGet(e){

var action = e.parameter.action;

  if(action == 'getItems'){
    return getItems(e);
  }  
  }


function addItem(e){

var date =  e.parameter.date;


var Date=e.parameter.date;
var Description = e.parameter.description;
var Amount = e.parameter.amount;
var CreditedOrDebited=e.parameter.credited;
var Type=e.parameter.type;
var Mode=e.parameter.modeOfPayment;
var Time=e.parameter.time;
var values;

if(CreditedOrDebited=="credited"){
 values = [Date,Description,"",Amount,Type,Mode,Time];  
}
else{
  values = [Date,Description,Amount,"",Type,Mode,Time];
}

sheet.appendRow(values);

return ContentService.createTextOutput("Success").setMimeType(ContentService.MimeType.TEXT);

}
function getItems(e){
  
  var records={};

  var rows = sheet.getRange(2, 1, sheet.getLastRow() - 1,sheet.getLastColumn()).getValues();
      data = [];

  for (var r = 0, l = rows.length; r < l; r++) {
    var row     = rows[r],
        record  = {};
    record['date'] = row[0];
    da=record['date'];
    record['description']=row[1];
    record['amount']=row[2];
    data.push(record);
    
   }
  records.items = data;
  var result=JSON.stringify(records);
  return ContentService.createTextOutput(result).setMimeType(ContentService.MimeType.JSON);
}
