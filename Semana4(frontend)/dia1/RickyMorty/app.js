console.log("Hello World Rick and Morty");

async function obtenerPersonajes() {
  const response = await fetch("https://rickandmortyapi.com/api/character");
  const data = await response.json();
  console.log("Personajes:", data.results);
  return data.results;
}

console.log(obtenerPersonajes());

function pintarPersonajes(personajes) {
  console.log("Pintando personajes:", personajes);
  let tarjetasHTML = "";
  console.log("Tarjetas HTML:", tarjetasHTML);
  personajes.forEach((personaje) => {
    tarjetasHTML += `
        <div class="card">
            <img src="${personaje.image}" alt="${personaje.name}">
            <h3>${personaje.name}</h3>
            <p class="species">${personaje.species}</p>
            <p class="status">${personaje.status}</p>
        </div>
        `;
  });
  document.getElementById("main-container").innerHTML = tarjetasHTML;
}

obtenerPersonajes().then(pintarPersonajes);
