window.onload = function (){
    console.log("window loaded");

    let nav_items = document.getElementsByClassName("nav-link");
    for(let nav_item of nav_items){
        nav_item.addEventListener(
            "click",
            showSingleTab
        );
    }
}

function showSingleTab(event) {
    let index = event.currentTarget.id.replace("select-tab-","");
    let tabs = document.getElementsByClassName("nav-link");
    for(let tab of tabs){
        if(tab.id==="select-tab-"+index){
            tab.classList.add("active");
        }else{
            tab.classList.remove("active");
        }
    }
    console.log("Show single tab: "+ index);
    updateCurrent(event.currentTarget);
    let tabsContent = document.getElementsByClassName("tab-pane");
    for(let tabContent of tabsContent){
        if(tabContent.id==="tab-"+index){
            tabContent.classList.remove("hidden");
        }else{
            tabContent.classList.add("hidden");
        }
    }
}

function updateCurrent(target) {
    let nav_items = document.getElementsByClassName("tab-pane");
    for(let nav_item of nav_items){
        if(target.id==="select-"+nav_item.id){
            nav_item.classList.add("active");
        }else{
            nav_item.classList.remove("active");
        }
    }

}