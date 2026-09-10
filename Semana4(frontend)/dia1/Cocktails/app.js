async function obtenerCocteles() {
  const response = await fetch(
    "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=margarita",
  );

  const data = await response.json();

  console.log("Cócteles:", data.drinks);

  return data.drinks;
}

function pintarCocteles(cocteles) {
  let tarjetasHTML = "";

  cocteles.forEach((coctel) => {
    tarjetasHTML += `
      <div class="card">
        <img src="${coctel.strDrinkThumb}" alt="${coctel.strDrink}">

        <h3>${coctel.strDrink}</h3>
        <p>${coctel.strCategory}</p>
        <p>${coctel.strAlcoholic}</p>
      </div>
    `;
  });

  document.getElementById("main-container").innerHTML = tarjetasHTML;
}

obtenerCocteles().then(pintarCocteles);
