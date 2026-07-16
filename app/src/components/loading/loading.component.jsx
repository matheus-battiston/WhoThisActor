import React from "react";
import "./loading.css";

const Loading = ({ frase }) => {
  return (
    <section className="loading-container" aria-live="polite" aria-busy="true">
      <div className="loading-shell">
        <div className="loading-spinner" aria-hidden="true">
          <span className="loading-spinner-ring loading-spinner-ring-primary"></span>
          <span className="loading-spinner-ring loading-spinner-ring-secondary"></span>
        </div>

        <p className="loading-text">{frase}</p>
      </div>
    </section>
  );
};

export default Loading;
