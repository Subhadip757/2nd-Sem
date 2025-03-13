import React, { useEffect, useState } from "react";
import { data } from "react-router-dom";

function Github() {
  const [data, setData] = useState([]);
  useEffect(() => {
    fetch("https://api.github.com/users/Subhadip757")
      .then((res) => res.json())
      .then((data) => {
        setData(data);
      });
  }, []);
  return (
    <div className="text-center text-2xl m-3 bg-gray-500 text-white p-5 flex flex-col items-center">
      Github Followers: {data.followers}
      <img
        src={data.avatar_url}
        alt="Git Picture"
        width={400}
        className="mt-3"
      />
    </div>
  );
}

export default Github;
