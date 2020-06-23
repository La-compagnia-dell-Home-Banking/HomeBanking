$(document).ready(() => {
  var accountID = GetParameterValues('account');
  var password = GetParameterValues('password');
  alert(accountID);
  function GetParameterValues(param) {
      var url = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
      for (var i = 0; i < url.length; i++) {
          var urlparam = url[i].split('=');
          if (urlparam[0] == param) {
              return urlparam[1];
          }
      }
  }


  fetch(`http://localhost:8080/HomeBanking/home-banking/persona/${accountID}`, {
      
  }).then(value => {
    return value.json().then(value => {
      console.log(value);
    })
  })





  async function getData() {
    $('#jquery').click( () => {
  $.ajax()
  .done( data => {
    // $("#quote").text(data[0])
    console.log(data.json());
  })
  .fail((err)=> {
    console.log(err.statusText)
  })
})
  }

  console.log(getData());
});


$(function() { /* code here */ });
