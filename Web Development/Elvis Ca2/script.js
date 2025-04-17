// Global variables
let currentStation = null;
let audioContext = null;
let analyser = null;
let stations = [];
let favorites = JSON.parse(localStorage.getItem("favorites")) || [];
let recentlyPlayed = JSON.parse(localStorage.getItem("recentlyPlayed")) || [];
let isPlaying = false;

// API Configuration
const API_CONFIG = {
    baseUrl: "https://de1.api.radio-browser.info/json",
    endpoints: {
        stations: "/stations",
        countries: "/countries",
        genres: "/tags",
    },
};

// Initialize when page loads
document.addEventListener("DOMContentLoaded", async () => {
    await initializeApp();
    setupEventListeners();
    setupDarkMode();
});

// Initialize application
async function initializeApp() {
    try {
        // Load initial data
        await Promise.all([loadStations(), loadCountries(), loadGenres()]);

        // Initialize audio context
        audioContext = new (window.AudioContext || window.webkitAudioContext)();
        analyser = audioContext.createAnalyser();
        analyser.fftSize = 256;

        // Setup visualizer
        setupVisualizer();

        // Update UI
        updateFavoritesList();
        updateRecentlyPlayedList();
    } catch (error) {
        showToast("Failed to initialize application", "danger");
        console.error("Initialization error:", error);
    }
}

// Load stations from API
async function loadStations() {
    try {
        const response = await fetch(
            `${API_CONFIG.baseUrl}${API_CONFIG.endpoints.stations}?limit=100&hidebroken=true`
        );
        if (!response.ok) throw new Error("Failed to fetch stations");

        stations = await response.json();
        renderStations(stations);
    } catch (error) {
        showToast("Failed to load stations", "danger");
        console.error("Error loading stations:", error);
    }
}

// Load countries for filter
async function loadCountries() {
    try {
        const response = await fetch(
            `${API_CONFIG.baseUrl}${API_CONFIG.endpoints.countries}`
        );
        if (!response.ok) throw new Error("Failed to fetch countries");

        const countries = await response.json();
        const countrySelect = document.getElementById("country-filter");

        countries.forEach((country) => {
            const option = document.createElement("option");
            option.value = country.name;
            option.textContent = country.name;
            countrySelect.appendChild(option);
        });
    } catch (error) {
        console.error("Error loading countries:", error);
    }
}

// Load genres for filter
async function loadGenres() {
    try {
        const response = await fetch(
            `${API_CONFIG.baseUrl}${API_CONFIG.endpoints.genres}`
        );
        if (!response.ok) throw new Error("Failed to fetch genres");

        const genres = await response.json();
        const genreSelect = document.getElementById("genre-filter");

        genres.forEach((genre) => {
            const option = document.createElement("option");
            option.value = genre.name;
            option.textContent = genre.name;
            genreSelect.appendChild(option);
        });
    } catch (error) {
        console.error("Error loading genres:", error);
    }
}

// Setup visualizer
function setupVisualizer() {
    const visualizer = document.getElementById("visualizer");
    const bufferLength = analyser.frequencyBinCount;
    const dataArray = new Uint8Array(bufferLength);

    // Create bars
    for (let i = 0; i < bufferLength; i++) {
        const bar = document.createElement("div");
        bar.className = "bar-visual";
        visualizer.appendChild(bar);
    }

    // Update visualizer
    function updateVisualizer() {
        if (!isPlaying) return;

        analyser.getByteFrequencyData(dataArray);
        const bars = visualizer.querySelectorAll(".bar-visual");

        bars.forEach((bar, i) => {
            const height = (dataArray[i] / 255) * 100;
            bar.style.height = `${height}%`;
        });

        requestAnimationFrame(updateVisualizer);
    }

    updateVisualizer();
}

// Play station
async function playStation(station) {
    try {
        const audio = document.getElementById("radio-player");

        // Stop current playback
        if (currentStation) {
            audio.pause();
            audio.src = "";
        }

        // Update current station
        currentStation = station;
        audio.src = station.url_resolved;

        // Connect to audio context
        const source = audioContext.createMediaElementSource(audio);
        source.connect(analyser);
        analyser.connect(audioContext.destination);

        // Play audio
        await audio.play();
        isPlaying = true;

        // Update UI
        updateNowPlaying(station);
        addToRecentlyPlayed(station);
        updatePlayButton();

        // Start visualizer
        setupVisualizer();

        showToast(`Now playing: ${station.name}`, "success");
    } catch (error) {
        showToast("Failed to play station", "danger");
        console.error("Error playing station:", error);
    }
}

// Stop playback
function stopPlayback() {
    const audio = document.getElementById("radio-player");
    audio.pause();
    audio.src = "";
    isPlaying = false;
    currentStation = null;

    updateNowPlaying();
    updatePlayButton();
}

// Toggle favorite
function toggleFavorite(station) {
    const index = favorites.findIndex(
        (f) => f.stationuuid === station.stationuuid
    );

    if (index === -1) {
        favorites.push(station);
        showToast("Added to favorites", "success");
    } else {
        favorites.splice(index, 1);
        showToast("Removed from favorites", "info");
    }

    localStorage.setItem("favorites", JSON.stringify(favorites));
    updateFavoritesList();
}

// Add to recently played
function addToRecentlyPlayed(station) {
    recentlyPlayed = recentlyPlayed.filter(
        (s) => s.stationuuid !== station.stationuuid
    );
    recentlyPlayed.unshift(station);
    recentlyPlayed = recentlyPlayed.slice(0, 10);

    localStorage.setItem("recentlyPlayed", JSON.stringify(recentlyPlayed));
    updateRecentlyPlayedList();
}

// Update UI functions
function updateNowPlaying(station = null) {
    const nowPlaying = document.getElementById("now-playing");
    const stationImage = document.getElementById("station-image");

    if (station) {
        nowPlaying.textContent = station.name;
        stationImage.src = station.favicon || "default-station.png";
    } else {
        nowPlaying.textContent = "Select a station to begin";
        stationImage.src = "default-station.png";
    }
}

function updatePlayButton() {
    const playBtn = document.getElementById("play-btn");
    const stopBtn = document.getElementById("stop-btn");

    if (isPlaying) {
        playBtn.innerHTML = '<i class="fas fa-pause"></i>';
        playBtn.classList.remove("btn-primary");
        playBtn.classList.add("btn-warning");
    } else {
        playBtn.innerHTML = '<i class="fas fa-play"></i>';
        playBtn.classList.remove("btn-warning");
        playBtn.classList.add("btn-primary");
    }

    stopBtn.disabled = !isPlaying;
}

function updateFavoritesList() {
    const favoritesList = document.getElementById("favorites-list");
    favoritesList.innerHTML = favorites
        .map(
            (station) => `
        <div class="station-item" onclick="playStation(${JSON.stringify(
            station
        )})">
            <img src="${
                station.favicon || "default-station.png"
            }" class="station-icon">
            <div class="station-info">
                <h6>${station.name}</h6>
                <small>${station.country}</small>
            </div>
            <button class="btn btn-sm btn-outline-danger" onclick="toggleFavorite(${JSON.stringify(
                station
            )})">
                <i class="fas fa-trash"></i>
            </button>
        </div>
    `
        )
        .join("");
}

function updateRecentlyPlayedList() {
    const recentList = document.getElementById("recently-played-list");
    recentList.innerHTML = recentlyPlayed
        .map(
            (station) => `
        <div class="station-item" onclick="playStation(${JSON.stringify(
            station
        )})">
            <img src="${
                station.favicon || "default-station.png"
            }" class="station-icon">
            <div class="station-info">
                <h6>${station.name}</h6>
                <small>${station.country}</small>
            </div>
        </div>
    `
        )
        .join("");
}

// Filter stations
function filterStations() {
    const searchTerm = document
        .getElementById("station-search")
        .value.toLowerCase();
    const country = document.getElementById("country-filter").value;
    const genre = document.getElementById("genre-filter").value;

    const filtered = stations.filter((station) => {
        const matchesSearch = station.name.toLowerCase().includes(searchTerm);
        const matchesCountry = country === "all" || station.country === country;
        const matchesGenre =
            genre === "all" ||
            (station.tags &&
                station.tags.toLowerCase().includes(genre.toLowerCase()));

        return matchesSearch && matchesCountry && matchesGenre;
    });

    renderStations(filtered);
}

// Render stations
function renderStations(stations) {
    const stationsList = document.getElementById("stations-list");
    stationsList.innerHTML = stations
        .map(
            (station) => `
        <div class="station-item" onclick="playStation(${JSON.stringify(
            station
        )})">
            <img src="${
                station.favicon || "default-station.png"
            }" class="station-icon">
            <div class="station-info">
                <h6>${station.name}</h6>
                <small>${station.country} • ${station.tags || "Music"}</small>
            </div>
            <button class="btn btn-sm btn-outline-warning" 
                    onclick="toggleFavorite(${JSON.stringify(station)})">
                <i class="far fa-star"></i>
            </button>
        </div>
    `
        )
        .join("");
}

// Setup event listeners
function setupEventListeners() {
    // Play/Pause button
    document.getElementById("play-btn").addEventListener("click", () => {
        if (currentStation) {
            if (isPlaying) {
                document.getElementById("radio-player").pause();
                isPlaying = false;
            } else {
                document.getElementById("radio-player").play();
                isPlaying = true;
            }
            updatePlayButton();
        }
    });

    // Stop button
    document.getElementById("stop-btn").addEventListener("click", stopPlayback);

    // Volume control
    document.getElementById("volume").addEventListener("input", (e) => {
        document.getElementById("radio-player").volume = e.target.value;
    });

    // Search and filters
    document
        .getElementById("station-search")
        .addEventListener("input", filterStations);
    document
        .getElementById("country-filter")
        .addEventListener("change", filterStations);
    document
        .getElementById("genre-filter")
        .addEventListener("change", filterStations);

    // Clear favorites
    document.getElementById("clear-favorites").addEventListener("click", () => {
        if (confirm("Are you sure you want to clear all favorites?")) {
            favorites = [];
            localStorage.setItem("favorites", JSON.stringify(favorites));
            updateFavoritesList();
            showToast("Favorites cleared", "info");
        }
    });
}

// Dark mode functionality
function setupDarkMode() {
    const darkModeToggle = document.getElementById("dark-mode-toggle");
    const isDarkMode = localStorage.getItem("darkMode") === "true";

    if (isDarkMode) {
        document.body.classList.add("dark-mode");
        darkModeToggle.innerHTML = '<i class="fas fa-sun"></i>';
    }

    darkModeToggle.addEventListener("click", () => {
        document.body.classList.toggle("dark-mode");
        const isDark = document.body.classList.contains("dark-mode");
        localStorage.setItem("darkMode", isDark);
        darkModeToggle.innerHTML = isDark
            ? '<i class="fas fa-sun"></i>'
            : '<i class="fas fa-moon"></i>';
    });
}

// Toast notifications
function showToast(message, type = "info") {
    const toastContainer = document.querySelector(".toast-container");
    const toast = document.createElement("div");
    toast.className = `toast show bg-${type} text-white`;
    toast.innerHTML = `
        <div class="toast-body">
            ${message}
        </div>
    `;

    toastContainer.appendChild(toast);
    setTimeout(() => toast.remove(), 3000);
}

// Error handling
window.addEventListener("error", (event) => {
    console.error("Global error:", event.error);
    showToast("An error occurred", "danger");
});

// Handle offline/online status
window.addEventListener("online", () => {
    showToast("Back online", "success");
    loadStations();
});

window.addEventListener("offline", () => {
    showToast("You are offline", "warning");
});
