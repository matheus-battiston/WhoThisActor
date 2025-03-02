import React from "react";
import "./loading.css";

const Loading = () => {
  return (
    <div className="loading-container">
      <div className="film-reel"></div>
      <p className="loading-text">Processando...</p>
    </div>
  );
};

export default Loading;
