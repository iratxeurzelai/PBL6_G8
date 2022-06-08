var btn1 = document.querySelector('#green');
var btn3 = document.querySelector('#green2');

btn1.addEventListener('click', function() {

  this.classList.toggle('green');
  
});

btn3.addEventListener('click', function() {
this.classList.toggle('green2');

});