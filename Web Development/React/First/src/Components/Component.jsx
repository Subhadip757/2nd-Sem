import React from "react";

function Component({ value, data }) {
  return (
    <>
      <div className="text-amber-400">
        <h1>{value}</h1>
        <h1>{data.age}</h1>
        <h1>{data.name}</h1>
      </div>
    </>
  );
}

export default Component;
