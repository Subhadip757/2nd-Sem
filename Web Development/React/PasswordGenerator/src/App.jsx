import React, { useCallback, useEffect, useState } from "react";
import { useRef } from "react";

export default function App() {
  const [length, setLength] = useState(8);
  const [number, setNumber] = useState(false);
  const [specialChar, setSpecialChar] = useState(false);
  const [password, setPassword] = useState("");
  const passwordRef = useRef(null);

  const copyPasswordToClipboard = useCallback(() => {
    if (password.current) {
      passwordRef.current.select();
      window.navigator.clipboard.writeText(password);
      alert("Password copied to clipboard");
    }
  }, [password]);

  const passwordGenerator = useCallback(() => {
    let generatedPass = "";
    let str = "ABCDEFGHIJKLMNOP";
    if (number) str += "0123456789";
    if (specialChar) str += "!@#$%^&*(){}[]";

    for (let i = 0; i < length; i++) {
      const randIdx = Math.floor(Math.random() * str.length + 1);
      generatedPass += str.charAt(randIdx);
    }
    setPassword(generatedPass);
  }, [length, number, specialChar, setPassword]);

  useEffect(() => {
    passwordGenerator();
  }, [length, number, specialChar, passwordGenerator]);

  return (
    <>
      <div className="w-screen h-screen bg-gradient-to-r from-gray-800 via-gray-900 to-black flex items-center justify-center text-white font-sans">
        {/* Card Container */}
        <div className="bg-gray-800 border border-gray-700 shadow-lg rounded-2xl p-8 w-96 flex flex-col items-center">
          {/* Header */}
          <div className="mb-6 text-center">
            <h1 className="text-4xl font-bold text-cyan-400 mb-2">
              Password Generator
            </h1>
            <p className="text-sm text-gray-300">
              Generate secure passwords with customizable options.
            </p>
          </div>

          {/* Password Display */}
          <div className="flex items-center w-full mb-6">
            <input
              ref={passwordRef}
              value={password}
              type="text"
              readOnly
              placeholder="Your password will appear here"
              className="w-full h-12 bg-gray-700 border border-gray-600 text-lg text-gray-200 rounded-l-lg px-4 focus:outline-none"
            />
            <button
              onClick={copyPasswordToClipboard}
              className="bg-cyan-500 hover:bg-cyan-400 text-black font-semibold h-12 px-4 rounded-r-lg"
            >
              Copy
            </button>
          </div>

          {/* Length Slider */}
          <div className="mb-6 w-full">
            <label
              htmlFor="rangeInput"
              className="block text-sm font-medium text-gray-300 mb-1"
            >
              Length: <span className="text-cyan-400">{length}</span>
            </label>
            <input
              type="range"
              id="rangeInput"
              className="w-full h-2 bg-gray-600 rounded-lg cursor-pointer accent-cyan-500"
              value={length}
              min={6}
              max={20}
              onChange={(e) => setLength(e.target.value)}
            />
          </div>

          {/* Checkboxes */}
          <div className="w-full flex flex-col gap-4">
            <div className="flex items-center">
              <input
                type="checkbox"
                id="numbersCheck"
                className="w-5 h-5 accent-cyan-500 mr-3"
                checked={number}
                onChange={() => setNumber(!number)}
              />
              <label
                htmlFor="numbersCheck"
                className="text-sm font-medium text-gray-300"
              >
                Include Numbers
              </label>
            </div>
            <div className="flex items-center">
              <input
                type="checkbox"
                id="charCheck"
                className="w-5 h-5 accent-cyan-500 mr-3"
                checked={specialChar}
                onChange={() => setSpecialChar(!specialChar)}
              />
              <label
                htmlFor="charCheck"
                className="text-sm font-medium text-gray-300"
              >
                Include Special Characters
              </label>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
