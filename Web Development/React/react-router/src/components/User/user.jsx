import React from "react";
import { useParams } from "react-router-dom";

function user() {
  const { id } = useParams();
  return (
    <div className="text-center text-2xl bg-gray-500 p-4 text-white">
      User: {id}
    </div>
  );
}

export default user;
