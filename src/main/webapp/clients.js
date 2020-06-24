//author oleskiy.OS
//This should be a client side, so we need to launch another localhost server
//to get it to work. Also i did not use React, because of
//project's time restrictions, so this is unstructured dummy pages with vanilla JS
//and jQuery scripts, just for client side testing purposes of our backend API routes
$(document).ready(() => {
  var name = document.querySelector(".personaCard h2");
  var bday = document.querySelector(".personaCard .bday");
  var container = document.querySelector(".data");

  function getData() {
    fetch(`http://localhost:8080/HomeBanking/home-banking/persona`, {
        "method": "GET",
        "headers": {
            "Access-Control-Request-Headers": "*",
            "Access-Control-Request-Method": "*"
        }
      })
      .then(value => {
        return value.json()
    }).then(value => {
        for(let i = 0; i < value.length; i++) {
          let createdDiv = document.createElement("div");
          createdDiv.setAttribute("class", "personaCard");
          parseObject(value[i], createdDiv);
        }
      });
  }

  function parseObject(data, createdDiv) {
    const {
      cognome: cognome,
      dataDiNascita: dataDiNascita,
      nome: nome
    } = data;
    createdDiv.innerHTML = `<header>
        <img src="https://pana-defenders.info/wp-content/themes/pana-defenders-2017/images/people-profile.jpg" alt="user's avatar">
        <h2>${nome} ${cognome}</h2>
      </header>
      <main class="personalInfo">
        <p class="bday"><strong>Birthdate:</strong> ${dataDiNascita}</p>
      </main>`
      container.appendChild(createdDiv);
  }

  getData();
});
