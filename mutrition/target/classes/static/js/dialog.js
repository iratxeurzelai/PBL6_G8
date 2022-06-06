var add,dialog,closeDialogBtn,cancel,ok,incidenceBtn,notificationBtn;
var dialogI,dialogN,closeDialogI,cancelI,okI;
var closeDialogI,cancelI,okI;

window.addEventListener("load",function(){
  add=document.getElementById("addBtnR");
  dialog=document.getElementById("DialogoNotificacion");
  dialogI=document.getElementById("DialogIncidence");
  dialogN=document.getElementById("DialogNotification");  
  closeDialogBtn=document.getElementsByClassName("btn-close");
  cancel=document.getElementsByClassName("cancel");
  ok=document.getElementsByClassName("ok");
  incidenceBtn=document.getElementById("btn-incidence");
  notificationBtn=document.getElementById("btn-notification");

  for (let index = 0; index < ok.length; index++) {
    const element = ok[index];
    element.addEventListener("click",function(){
      if (element.id=='okI') {
        dialogI.close();
      }
      else if(element.id=='okN'){
        dialogN.close();
      }
      else{
        dialog.close();
      }
    });
  }

  for (let index = 0; index < cancel.length; index++) {
    const element = cancel[index];
    element.addEventListener("click",function(){
      if (element.id=='cancelI') {
        dialogI.close();
      }
      else if(element.id=='cancelN'){
        dialogN.close();
      }
      else{
        dialog.close();
      }
    });
  }

  for (let index = 0; index < closeDialogBtn.length; index++) {
    const element = closeDialogBtn[index];
    element.addEventListener("click",function(){
      if (element.id=='btn-closeI') {
        dialogI.close();
      }
      else if(element.id=='btn-closeN'){
        dialogN.close();
      }
      else{
        dialog.close();
      }
    });
  }

  if(add!=null){
    add.addEventListener("click",function(){
      dialog.showModal();
     
    }) ;
  }
  
  if(incidenceBtn!=null){
    incidenceBtn.addEventListener("click",function(){
      dialogI.showModal();
     
    }) ;
  }

  
  if(notificationBtn!=null){
    notificationBtn.addEventListener("click",function(){
      dialogN.showModal();
     
    }) ;
  }

  

});