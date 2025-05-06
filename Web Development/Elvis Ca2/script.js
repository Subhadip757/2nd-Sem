// DOM Elements
const radioPlayer = document.getElementById('radio-player');
const playBtn = document.getElementById('play-btn');
const stopBtn = document.getElementById('stop-btn');
const favoriteBtn = document.getElementById('favorite-btn');
const volumeControl = document.getElementById('volume');
const stationSearch = document.getElementById('station-search');
const searchBtn = document.getElementById('search-btn');
const countryFilter = document.getElementById('country-filter');
const genreFilter = document.getElementById('genre-filter');
const stationsList = document.getElementById('stations-list');
const favoritesList = document.getElementById('favorites-list');
const recentlyPlayedList = document.getElementById('recently-played-list');
const clearFavoritesBtn = document.getElementById('clear-favorites');
const darkModeToggle = document.getElementById('dark-mode-toggle');
const nowPlaying = document.getElementById('now-playing');
const stationName = document.getElementById('station-name');
const stationLocation = document.getElementById('station-location');
const signalStrength = document.getElementById('signal-strength');
const visualizer = document.getElementById('visualizer');
const menuToggle = document.querySelector('.menu-toggle');
const sidebar = document.querySelector('.sidebar');
const navbarSearch = document.querySelector('.search-box input');

// State
let currentStation = null;
let favorites = JSON.parse(localStorage.getItem('favorites')) || [];
let recentlyPlayed = JSON.parse(localStorage.getItem('recentlyPlayed')) || [];
let isPlaying = false;
let audioContext;
let analyser;
let dataArray;
let animationId;

// Initialize
init();

function init() {
    // Set up event listeners
    playBtn.addEventListener('click', togglePlay);
    stopBtn.addEventListener('click', stopRadio);
    favoriteBtn.addEventListener('click', toggleFavorite);
    volumeControl.addEventListener('input', setVolume);
    searchBtn.addEventListener('click', searchStations);
    clearFavoritesBtn.addEventListener('click', clearFavorites);
    darkModeToggle.addEventListener('click', toggleDarkMode);
    menuToggle.addEventListener('click', toggleSidebar);
    navbarSearch.addEventListener('input', handleNavbarSearch);

    // Load favorites and recently played
    loadFavorites();
    loadRecentlyPlayed();
    loadCountries();

    // Set initial volume
    radioPlayer.volume = volumeControl.value;

    // Initialize audio context for visualizer
    initAudioContext();

    // Handle sidebar menu clicks
    document.querySelectorAll('.sidebar-menu a').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const target = e.currentTarget.getAttribute('href').substring(1);
            handleMenuNavigation(target);
        });
    });
}

// Sidebar and Navigation
function toggleSidebar() {
    sidebar.classList.toggle('active');
}

function handleMenuNavigation(target) {
    // Remove active class from all menu items
    document.querySelectorAll('.sidebar-menu li').forEach(item => {
        item.classList.remove('active');
    });

    // Add active class to clicked menu item
    const activeItem = document.querySelector(`.sidebar-menu a[href="#${target}"]`).parentElement;
    activeItem.classList.add('active');

    // Handle navigation based on target
    switch(target) {
        case 'home':
            // Scroll to top
            window.scrollTo({ top: 0, behavior: 'smooth' });
            break;
        case 'explore':
            // Focus on search
            stationSearch.focus();
            break;
        case 'favorites':
            // Scroll to favorites section
            document.querySelector('.favorites-container').scrollIntoView({ behavior: 'smooth' });
            break;
        case 'recent':
            // Scroll to recently played section
            document.querySelector('.recently-played').scrollIntoView({ behavior: 'smooth' });
            break;
        case 'settings':
            // Show settings modal (to be implemented)
            alert('Settings will be implemented soon!');
            break;
    }

    // Close sidebar on mobile
    if (window.innerWidth <= 768) {
        toggleSidebar();
    }
}

function handleNavbarSearch(e) {
    const searchTerm = e.target.value;
    if (searchTerm.length >= 3) {
        stationSearch.value = searchTerm;
        searchStations();
    }
}

// Audio Context and Visualizer
function initAudioContext() {
    audioContext = new (window.AudioContext || window.webkitAudioContext)();
    analyser = audioContext.createAnalyser();
    analyser.fftSize = 256;
    const bufferLength = analyser.frequencyBinCount;
    dataArray = new Uint8Array(bufferLength);
}

function updateVisualizer() {
    if (!isPlaying) return;

    analyser.getByteFrequencyData(dataArray);
    const canvas = document.createElement('canvas');
    const ctx = canvas.getContext('2d');
    canvas.width = visualizer.clientWidth;
    canvas.height = visualizer.clientHeight;
    visualizer.innerHTML = '';
    visualizer.appendChild(canvas);

    const barWidth = canvas.width / dataArray.length;
    let x = 0;

    ctx.fillStyle = 'rgba(0, 0, 0, 0.1)';
    ctx.fillRect(0, 0, canvas.width, canvas.height);

    for (let i = 0; i < dataArray.length; i++) {
        const barHeight = (dataArray[i] / 255) * canvas.height;
        const gradient = ctx.createLinearGradient(0, 0, 0, canvas.height);
        gradient.addColorStop(0, '#4a90e2');
        gradient.addColorStop(1, '#2c3e50');
        ctx.fillStyle = gradient;
        ctx.fillRect(x, canvas.height - barHeight, barWidth - 1, barHeight);
        x += barWidth;
    }

    animationId = requestAnimationFrame(updateVisualizer);
}

// Radio Controls
function togglePlay() {
    if (!currentStation) {
        alert('Please select a station first');
        return;
    }

    if (isPlaying) {
        radioPlayer.pause();
        isPlaying = false;
        playBtn.innerHTML = '<i class="fas fa-play"></i> Play';
        cancelAnimationFrame(animationId);
    } else {
        radioPlayer.play();
        isPlaying = true;
        playBtn.innerHTML = '<i class="fas fa-pause"></i> Pause';
        updateVisualizer();
    }
}

function stopRadio() {
    radioPlayer.pause();
    radioPlayer.currentTime = 0;
    isPlaying = false;
    playBtn.innerHTML = '<i class="fas fa-play"></i> Play';
    cancelAnimationFrame(animationId);
}

function setVolume() {
    radioPlayer.volume = volumeControl.value;
}

// Station Management
async function searchStations() {
    const searchTerm = stationSearch.value;
    const country = countryFilter.value;
    const genre = genreFilter.value;

    let url = 'https://de1.api.radio-browser.info/json/stations/search?';
    if (searchTerm) url += `name=${encodeURIComponent(searchTerm)}&`;
    if (country !== 'all') url += `country=${encodeURIComponent(country)}&`;
    if (genre !== 'all') url += `tag=${encodeURIComponent(genre)}&`;
    url += 'limit=50&hidebroken=true';

    try {
        const response = await fetch(url);
        const stations = await response.json();
        displayStations(stations);
    } catch (error) {
        console.error('Error fetching stations:', error);
        alert('Error fetching stations. Please try again.');
    }
}

function displayStations(stations) {
    stationsList.innerHTML = '';
    stations.forEach(station => {
        const stationElement = document.createElement('div');
        stationElement.className = 'station-item';
        stationElement.innerHTML = `
            <h5>${station.name}</h5>
            <p class="text-muted">${station.country} - ${station.tags}</p>
        `;
        stationElement.addEventListener('click', () => playStation(station));
        stationsList.appendChild(stationElement);
    });
}

function playStation(station) {
    currentStation = station;
    radioPlayer.src = station.url;
    nowPlaying.textContent = station.name;
    stationName.textContent = station.name;
    stationLocation.textContent = `${station.country} - ${station.tags}`;
    
    // Update favorite button state
    favoriteBtn.innerHTML = favorites.some(f => f.id === station.id) 
        ? '<i class="fas fa-star"></i> Unfavorite'
        : '<i class="far fa-star"></i> Favorite';

    // Add to recently played
    addToRecentlyPlayed(station);
    
    // Play the station
    togglePlay();
}

// Favorites Management
function toggleFavorite() {
    if (!currentStation) return;

    const index = favorites.findIndex(f => f.id === currentStation.id);
    if (index === -1) {
        favorites.push(currentStation);
        favoriteBtn.innerHTML = '<i class="fas fa-star"></i> Unfavorite';
    } else {
        favorites.splice(index, 1);
        favoriteBtn.innerHTML = '<i class="far fa-star"></i> Favorite';
    }

    localStorage.setItem('favorites', JSON.stringify(favorites));
    loadFavorites();
}

function loadFavorites() {
    favoritesList.innerHTML = '';
    favorites.forEach(station => {
        const stationElement = document.createElement('div');
        stationElement.className = 'favorite-item';
        stationElement.innerHTML = `
            <h5>${station.name}</h5>
            <p class="text-muted">${station.country} - ${station.tags}</p>
        `;
        stationElement.addEventListener('click', () => playStation(station));
        favoritesList.appendChild(stationElement);
    });
}

function clearFavorites() {
    if (confirm('Are you sure you want to clear all favorites?')) {
        favorites = [];
        localStorage.setItem('favorites', JSON.stringify(favorites));
        loadFavorites();
        if (currentStation) {
            favoriteBtn.innerHTML = '<i class="far fa-star"></i> Favorite';
        }
    }
}

// Recently Played Management
function addToRecentlyPlayed(station) {
    recentlyPlayed = recentlyPlayed.filter(s => s.id !== station.id);
    recentlyPlayed.unshift(station);
    if (recentlyPlayed.length > 10) {
        recentlyPlayed.pop();
    }
    localStorage.setItem('recentlyPlayed', JSON.stringify(recentlyPlayed));
    loadRecentlyPlayed();
}

function loadRecentlyPlayed() {
    recentlyPlayedList.innerHTML = '';
    recentlyPlayed.forEach(station => {
        const stationElement = document.createElement('div');
        stationElement.className = 'recent-item';
        stationElement.innerHTML = `
            <h5>${station.name}</h5>
            <p class="text-muted">${station.country} - ${station.tags}</p>
        `;
        stationElement.addEventListener('click', () => playStation(station));
        recentlyPlayedList.appendChild(stationElement);
    });
}

// Country List Management
async function loadCountries() {
    try {
        const response = await fetch('https://de1.api.radio-browser.info/json/countries');
        const countries = await response.json();
        countries.forEach(country => {
            const option = document.createElement('option');
            option.value = country.name;
            option.textContent = country.name;
            countryFilter.appendChild(option);
        });
    } catch (error) {
        console.error('Error fetching countries:', error);
    }
}

// Dark Mode
function toggleDarkMode() {
    const currentTheme = document.documentElement.getAttribute('data-theme');
    const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
    document.documentElement.setAttribute('data-theme', newTheme);
    localStorage.setItem('theme', newTheme);
    darkModeToggle.textContent = newTheme === 'dark' ? '☀️ Toggle Light Mode' : '🌙 Toggle Dark Mode';
}

// Initialize dark mode from localStorage
const savedTheme = localStorage.getItem('theme') || 'light';
document.documentElement.setAttribute('data-theme', savedTheme);
darkModeToggle.textContent = savedTheme === 'dark' ? '☀️ Toggle Light Mode' : '�� Toggle Dark Mode'; 