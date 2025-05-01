import { useEffect, useState } from "react";
export default function CartCount() {
  const [count, setCount] = useState(0);
  useEffect(() => {
    function update() {
      const cart = JSON.parse(localStorage.getItem("cart")) || [];
      setCount(cart.reduce((t, i) => t + i.quantity, 0));
    }
    update();
    window.addEventListener("storage", update);
    window.addEventListener("cartUpdated", update);
    return () => {
      window.removeEventListener("storage", update);
      window.removeEventListener("cartUpdated", update);
    };
  }, []);
  return (
    <span className="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger cart-count">
      {count}
    </span>
  );
}
