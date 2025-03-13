import React, { useState } from "react";
import Component from "./Components/Component";

export default function App() {
  let [a, b] = useState(50);
  const [c, d] = useState(false);

  return (
    <>
      <div className="bg-zinc-900 w-full h-screen p-5 text-white">
        <h1>{a}</h1>
        <button
          onClick={() => b(a + 1)}
          className="p-2 w-24 h-16 bg-green-400 rounded-xl"
        >
          Increase Count
        </button>

        <button
          onClick={() => b(a - 1)}
          className="p-2 w-24 h-16 bg-green-400 rounded-xl"
        >
          Decrease Count
        </button>
        <Component value="Component" data={{ age: 21, name: "Subhadip" }} />

        <h1>{c === false ? "Hello" : "hey"}</h1>
        <button
          onClick={() => d(!c)}
          className="w-24 h-16 bg-green-700 rounded-xl"
        >
          Click to change value
        </button>
      </div>
    </>
  );
}
