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
