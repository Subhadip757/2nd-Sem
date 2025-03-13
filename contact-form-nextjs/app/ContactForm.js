"use client";

import { useState } from "react";
import QRCode from "react-qr-code"; // ✅ Correct import

export default function ContactForm() {
  const [showForm, setShowForm] = useState(false);
  const [showQR, setShowQR] = useState(false);
  const [formData, setFormData] = useState({
    name: "",
    email: "",
    contact: "",
    branch: "",
    idcard: null,
  });
  const [message, setMessage] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleFileChange = (e) => {
    setFormData({ ...formData, idcard: e.target.files[0] });
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-center text-2xl font-bold">Contact Us</h1>

      <div className="flex justify-center space-x-4 my-4">
        <button
          className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition"
          onClick={() => {
            setShowForm(true);
            setShowQR(false);
          }}
        >
          Show Form
        </button>
        <button
          className="bg-gray-500 text-white px-4 py-2 rounded-md hover:bg-gray-600 transition"
          onClick={() => {
            setShowQR(true);
            setShowForm(false);
          }}
        >
          Show QR Code
        </button>
      </div>

      {showQR && (
        <div className="flex justify-center p-4 bg-gray-100 rounded-md shadow-md">
          <QRCode
            value="https://docs.google.com/forms/d/e/1FAIpQLSf-xkNetZJ2tIatdIWq_SaWXelV2HaQmdbBmNUvGodAFurngg/viewform?usp=header"
            size={200}
          />
        </div>
      )}

      {showForm && (
        <form
          method="post"
          action="https://script.google.com/macros/s/AKfycbyWnwvv62fidCTK-kGChjygQc2_AX0rhpLcs1x4WkR29slPkBviiqPclSC-fzbAbZq-BA/exec"
          className="max-w-lg mx-auto bg-white p-6 shadow-md rounded-md"
        >
          <label className="block text-gray-700">Name</label>
          <input
            type="text"
            name="name"
            onChange={handleChange}
            required
            className="w-full p-2 border rounded-md mb-2"
          />

          <label className="block text-gray-700">Email</label>
          <input
            type="email"
            name="email"
            onChange={handleChange}
            required
            className="w-full p-2 border rounded-md mb-2"
          />

          <label className="block text-gray-700">Contact</label>
          <input
            type="tel"
            name="contact"
            onChange={handleChange}
            required
            className="w-full p-2 border rounded-md mb-2"
          />

          <label className="block text-gray-700">Branch</label>
          <select
            name="branch"
            onChange={handleChange}
            required
            className="w-full p-2 border rounded-md mb-2"
          >
            <option value="">Select your branch</option>
            <option value="BCA">BCA</option>
            <option value="MCA">MCA</option>
            <option value="BTECH">B.Tech</option>
            <option value="MTECH">M.Tech</option>
            <option value="BSCIT">B.Sc IT</option>
            <option value="MSCIT">M.Sc IT</option>
          </select>

          <label className="block text-gray-700">Upload ID Card</label>
          <input
            type="file"
            name="idcard"
            onChange={handleFileChange}
            required
            className="w-full p-2 border rounded-md mb-2"
          />

          <button
            type="submit"
            className="bg-green-500 text-white px-4 py-2 rounded-md hover:bg-green-600 transition w-full"
          >
            Submit
          </button>

          {message && (
            <p className="text-center text-green-600 mt-2">{message}</p>
          )}
        </form>
      )}
    </div>
  );
}
