//author oleskiy.OS
//This should be a client side, so we need to launch another localhost server
//to get it to work. Also i did not use React, because of
//project's time restrictions, so this is very dummy pages with vanilla JS
//and jQuery scripts, just for client side testing purposes of our backend API routes
$(document).ready(() => {
  var accountID = GetParameterValues('account');
  var password = GetParameterValues('password');

  var welcome = document.querySelector("#welcome");
  var name = document.querySelector(".personaCard h2");
  var bday = document.querySelector(".personaCard .bday");
  var postaElettr = document.querySelector(".personaCard .email");
  var tel = document.querySelector(".personaCard .tel");
  var addr = document.querySelector(".personaCard address");

  function GetParameterValues(param) {
      var url = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
      for (var i = 0; i < url.length; i++) {
          var urlparam = url[i].split('=');
          if (urlparam[0] == param) {
              return urlparam[1];
          }
      }
  }
alert(accountID);
function getData(path = `persona/${accountID}`,) {
  fetch(`http://localhost:8080/HomeBanking/${path}`, {
      "method": "GET",
      "headers": {
          "Access-Control-Request-Headers": "*",
          "Access-Control-Request-Method": "*"
      }
    })
    .then(value => {
      return value.json()
  }).then(value => {
      parseObject(value)
      // console.log(value);
    });
}


  function parseObject(data) {
    const {
      cap: cap,
      cognome: cognome,
      dataDiNascita: dataDiNascita,
      email: email,
      indirizzo: indirizzo,
      luogoDiNascita: luogoDiNascita,
      nome: nome,
      persona_id: personaId,
      residenza: residenza,
      telefono: telefono,
      docs: { codice_fiscale: cf },
      docs: { document: document }
     } = data;
     welcome.innerText = `Hello, ${nome} ${cognome}!`;
     name.innerText = `${nome} ${cognome}`;
     bday.innerHTML = `<strong>Birthdate:</strong> ${dataDiNascita}, ${luogoDiNascita}`;
     postaElettr.innerHTML = `<strong>Email:</strong> ${email}`;
     tel.innerHTML = `<strong>Telefono:</strong> ${telefono}`;
     addr.innerHTML = `<strong>Indirizzo:</strong><br>
                        Example.com<br>
                        ${indirizzo}<br>
                        <strong>Residenza:</strong><br>
                        ${residenza}<br>`;
  }

  getData();
})



//
// $(function() { /* code here */ });
