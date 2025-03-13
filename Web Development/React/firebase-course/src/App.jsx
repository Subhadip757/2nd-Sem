import React, { useEffect, useState } from "react";
import { Auth } from "./components/auth";
import { db } from "./config/firebase";
import {
  getDocs,
  collection,
  addDoc,
  deleteDoc,
  doc,
  updateDoc,
} from "firebase/firestore";

function App() {
  const [movielist, setmovielist] = useState([]);
  const [newMovieTitle, setNewMovieTitle] = useState("");
  const [newReleasedate, setNewReleaseDate] = useState("");
  const [oscarRecieved, setOscarRecieved] = useState(false);
  const [updatedTitles, setUpdatedTitles] = useState({}); // Stores titles for each movie

  const movieColectionRef = collection(db, "movies");

  // Fetch movies from Firestore
  const getMovieList = async () => {
    try {
      const data = await getDocs(movieColectionRef);
      const filteredData = data.docs.map((doc) => ({
        ...doc.data(),
        id: doc.id,
      }));
      setmovielist(filteredData);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    getMovieList();
  }, []);

  // Add a new movie
  const onSubmitMovie = async () => {
    try {
      await addDoc(movieColectionRef, {
        title: newMovieTitle,
        date: newReleasedate,
        recievedAnOscar: oscarRecieved,
      });
      setNewMovieTitle("");
      setNewReleaseDate("");
      setOscarRecieved(false);
      getMovieList(); // Refresh list
    } catch (err) {
      console.error(err);
    }
  };

  // Delete a movie
  const deleteMovie = async (id) => {
    try {
      const movieDoc = doc(db, "movies", id);
      await deleteDoc(movieDoc);
      getMovieList(); // Refresh after delete
    } catch (err) {
      console.error("Error deleting movie:", err);
    }
  };

  // Update movie title
  const updateMovieTitle = async (id) => {
    if (!updatedTitles[id]) {
      alert("Enter a new title before updating.");
      return;
    }
    try {
      const movieDoc = doc(db, "movies", id);
      await updateDoc(movieDoc, { title: updatedTitles[id] });
      setUpdatedTitles((prev) => ({ ...prev, [id]: "" })); // Clear input for updated movie
      getMovieList(); // Refresh list after update
    } catch (err) {
      console.error("Error updating title:", err);
    }
  };

  return (
    <>
      <Auth />
      <div className="flex flex-col justify-center items-center space-y-3 p-5">
        <input
          className="border p-2 w-64"
          type="text"
          placeholder="Movie Title..."
          value={newMovieTitle}
          onChange={(e) => setNewMovieTitle(e.target.value)}
        />
        <input
          className="border p-2 w-64"
          type="number"
          placeholder="Release Date..."
          value={newReleasedate}
          onChange={(e) => setNewReleaseDate(e.target.value)}
        />
        <div className="flex items-center">
          <input
            className="mr-2"
            type="checkbox"
            checked={oscarRecieved}
            onChange={(e) => setOscarRecieved(e.target.checked)}
          />
          <label>Received an Oscar</label>
        </div>
        <button
          onClick={onSubmitMovie}
          className="border p-2 bg-blue-500 text-white w-64"
        >
          Submit Movie
        </button>
      </div>

      <div className="p-5">
        {movielist.map((movie) => (
          <div key={movie.id} className="text-center border p-4 m-3">
            <h1 style={{ color: movie.recievedAnOscar ? "green" : "red" }}>
              {movie.title}
            </h1>
            <p>Date: {movie.date}</p>
            <input
              className="border p-2 w-64 mt-2"
              type="text"
              placeholder="Update title..."
              value={updatedTitles[movie.id] || ""}
              onChange={(e) =>
                setUpdatedTitles((prev) => ({
                  ...prev,
                  [movie.id]: e.target.value,
                }))
              }
            />
            <div className="flex justify-center space-x-2 mt-2">
              <button
                onClick={() => updateMovieTitle(movie.id)}
                className="border p-2 bg-yellow-500 text-white"
              >
                Update Title
              </button>
              <button
                onClick={() => deleteMovie(movie.id)}
                className="border p-2 bg-red-500 text-white"
              >
                Delete Movie
              </button>
            </div>
          </div>
        ))}
      </div>
    </>
  );
}

export default App;
