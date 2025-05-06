import React, {useEffect, useState, useRef} from "react";
import Sidebar from './components/Sidebar';
import Favorites from './components/Favorites'
import Footer from "./Components/Footer";
import Navbar from "./components/Navbar";
import Player from "./components/Player";
import RecentlyPlayed from "./components/RecentlyPlayed";
import StationSearch from "./components/StationSearch";
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import './style.css';

function App() {
  // --- State ---
  const [darkMode, setDarkMode] = useState(localStorage.getItem('theme') === 'dark');
  const [stationSearch, setStationSearch] = useState("");
  const [countryList, setCountryList] = useState([]);
  const [countryFilter, setCountryFilter] = useState("all");
  const [genreFilter, setGenreFilter] = useState("all");
  const [stations, setStations] = useState([]);
  const [favorites, setFavorites] = useState(() => JSON.parse(localStorage.getItem('favorites')) || []);
  const [recentlyPlayed, setRecentlyPlayed] = useState(() => JSON.parse(localStorage.getItem('recentlyPlayed')) || []);
  const [currentStation, setCurrentStation] = useState(null);
  const [isPlaying, setIsPlaying] = useState(false);
  const [volume, setVolume] = useState(0.7);
  const [loadingStations, setLoadingStations] = useState(false);
  const [stationsError, setStationsError] = useState(null);

  // --- Refs ---
  const audioRef = useRef(null);
  const visualizerRef = useRef(null);

  // --- Effects ---
  useEffect(() => {
    document.documentElement.setAttribute('data-theme', darkMode ? 'dark' : 'light');
    localStorage.setItem('theme', darkMode ? 'dark' : 'light');
  }, [darkMode]);

  useEffect(() => {
    fetch('https://de1.api.radio-browser.info/json/countries')
      .then(res => res.json())
      .then(data => setCountryList(data.map(c => c.name)))
      .catch(() => setCountryList([]));
  }, []);

  useEffect(() => {
    localStorage.setItem('favorites', JSON.stringify(favorites));
  }, [favorites]);

  useEffect(() => {
    localStorage.setItem('recentlyPlayed', JSON.stringify(recentlyPlayed));
  }, [recentlyPlayed]);

  useEffect(() => {
    handleSearchStations();
    // eslint-disable-next-line
  }, []);

  // --- Handlers ---
  const handleMenuClick = e => {
    e.preventDefault();
    // You can implement scroll or navigation logic here if needed
  };

  const handleToggleDarkMode = () => setDarkMode(d => !d);

  const handleMenuToggle = () => {
    document.querySelector('.sidebar').classList.toggle('active');
  };

  const handleSearchStations = () => {
    setLoadingStations(true);
    setStationsError(null);
    let url = 'https://de1.api.radio-browser.info/json/stations/search?';
    if (stationSearch) url += `name=${encodeURIComponent(stationSearch)}&`;
    if (countryFilter !== 'all') url += `country=${encodeURIComponent(countryFilter)}&`;
    if (genreFilter !== 'all') url += `tag=${encodeURIComponent(genreFilter)}&`;
    url += 'limit=50&hidebroken=true';

    fetch(url)
      .then(res => res.json())
      .then(data => setStations(data))
      .catch(() => setStationsError("Failed to fetch stations."))
      .finally(() => setLoadingStations(false));
  };

  const handlePlayStation = station => {
    setCurrentStation(station);
    setIsPlaying(true);
    // Add to recently played
    setRecentlyPlayed(prev => {
      const filtered = prev.filter(s => s.id !== station.id);
      const updated = [station, ...filtered];
      return updated.slice(0, 10);
    });
  };

  const handlePlay = () => {
    if (!currentStation) {
      alert('Please select a station first');
      return;
    }
    setIsPlaying(p => !p);
  };

  const handleStop = () => {
    setIsPlaying(false);
    if (audioRef.current) {
      audioRef.current.pause();
      audioRef.current.currentTime = 0;
    }
  };

  const handleFavorite = () => {
    if (!currentStation) return;
    setFavorites(prev => {
      const exists = prev.some(f => f.id === currentStation.id);
      if (exists) {
        toast.info("Removed from favorites");
        return prev.filter(f => f.id !== currentStation.id);
      } else {
        toast.success("Added to favorites!");
        return [...prev, currentStation];
      }
    });
  };

  const handleClearFavorites = () => {
    if (window.confirm('Are you sure you want to clear all favorites?')) {
      setFavorites([]);
    }
  };

  const handleVolumeChange = e => {
    setVolume(Number(e.target.value));
    if (audioRef.current) {
      audioRef.current.volume = Number(e.target.value);
    }
  };

  // --- Audio Effect ---
  useEffect(() => {
    if (audioRef.current && currentStation) {
      audioRef.current.src = currentStation.url;
      audioRef.current.volume = volume;
      if (isPlaying) {
        audioRef.current.play();
      } else {
        audioRef.current.pause();
      }
    }
  }, [currentStation, isPlaying, volume]);

  // --- Visualizer (Optional, basic) ---
  // You can implement a canvas visualizer here if you want

  // --- Render ---
  return (
    <div className="d-flex">
      <div className="animated-background"></div>
      <Sidebar onMenuClick={handleMenuClick} onToggleDarkMode={handleToggleDarkMode} darkMode={darkMode} />
      <div className="main-content flex-grow-1">
        <Navbar onMenuToggle={handleMenuToggle} onSearch={e => {
          setStationSearch(e.target.value);
          if (e.target.value.length >= 3) handleSearchStations();
        }} />
        <div className="content-wrapper">
          <header className="text-center py-5 gradient-bg glass-morphism">
            <div className="container">
              <h1 className="display-4 text-white fw-bold mb-3">Echo Sphere</h1>
              <p className="lead text-white-50">Explore radio stations across the globe</p>
            </div>
          </header>
          <main className="container py-5">
            <div className="row justify-content-center">
              <div className="col-lg-10">
                <Player
                  nowPlaying={currentStation ? currentStation.name : "Select a station to begin"}
                  volume={volume}
                  onVolumeChange={handleVolumeChange}
                  onPlay={handlePlay}
                  onStop={handleStop}
                  onFavorite={handleFavorite}
                  isPlaying={isPlaying}
                  isFavorite={currentStation && favorites.some(f => f.id === currentStation.id)}
                  stationName={currentStation ? currentStation.name : "-"}
                  stationLocation={currentStation ? `${currentStation.country} - ${currentStation.tags}` : "-"}
                  visualizerRef={visualizerRef}
                />
                <div className="row g-4">
                  <div className="col-md-6">
                    <StationSearch
                      stationSearch={stationSearch}
                      setStationSearch={setStationSearch}
                      onSearchStations={handleSearchStations}
                      countryList={countryList}
                      countryFilter={countryFilter}
                      setCountryFilter={setCountryFilter}
                      genreFilter={genreFilter}
                      setGenreFilter={setGenreFilter}
                      stations={stations}
                      onPlayStation={handlePlayStation}
                      loadingStations={loadingStations}
                      stationsError={stationsError}
                    />
                  </div>
                  <div className="col-md-6">
                    <Favorites
                      favorites={favorites}
                      onPlayStation={handlePlayStation}
                      onClearFavorites={handleClearFavorites}
                    />
                    <RecentlyPlayed
                      recentlyPlayed={recentlyPlayed}
                      onPlayStation={handlePlayStation}
                    />
                  </div>
                </div>
              </div>
            </div>
          </main>
          <Footer />
        </div>
      </div>
      <audio ref={audioRef} preload="none" style={{ display: "none" }} />
      <ToastContainer position="bottom-right" />
    </div>
  );
}

export default App;
