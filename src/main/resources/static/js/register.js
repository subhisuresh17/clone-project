document.addEventListener("DOMContentLoaded", function () {
  const searchInput = document.getElementById("company-search");
  const companyResults = document.getElementById("company-results");
  const companyIdInput = document.querySelector('input[name="company.id"]'); // Modified to match the company attribute

  searchInput.addEventListener("input", function () {
    const searchText = searchInput.value.trim().toLowerCase();

    // Call your endpoint to get company list
    fetch("/company/allCompanies")
      .then((response) => {
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        return response.json();
      })
      .then((companies) => {
        companyResults.innerHTML = "";

        const filteredCompanies = companies.filter((company) => {
          return company.name.toLowerCase().includes(searchText);
        });

        if (filteredCompanies.length === 0) {
          companyResults.innerHTML =
            '<div class="company-result">No companies found</div>';
        } else {
          filteredCompanies.forEach((company) => {
            const companyResult = document.createElement("div");
            companyResult.classList.add("company-result");
            companyResult.textContent = company.name;
            companyResult.setAttribute("data-company-id", company.id); // Add company ID as attribute
            companyResult.addEventListener("click", function (event) {
              event.preventDefault(); // Prevent default action
              searchInput.value = company.name;
              companyIdInput.value = company.id; // Set the company ID to hidden input field
              companyResults.innerHTML = ""; // Clear the results
            });
            companyResults.appendChild(companyResult);
          });
        }
      })
      .catch((error) => {
        console.error("Error fetching companies:", error);
      });
  });

  // Add event listener to hide dropdown and clear input field when clicking outside of them
  document.addEventListener("click", function (event) {
    if (
      !companyResults.contains(event.target) &&
      event.target !== searchInput
    ) {
      companyResults.innerHTML = ""; // Clear the results
    }
  });
});
