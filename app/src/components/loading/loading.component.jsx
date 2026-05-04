import React from "react";
import "./loading.css";

const Loading = ({ frase }) => {
  // return (
  //   <div className="loading-container">
  //     <div className="film-reel"></div>
  //     <p className="loading-text">Processando...</p>
  //   </div>
  // );

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

  //   return (
  //     <section className="loading-container" aria-live="polite" aria-busy="true">
  //       <div className="loading-backdrop"></div>
  //
  //       <div className="loading-card">
  //         <div className="loading-orbital">
  //           <div className="loading-orbit loading-orbit-primary"></div>
  //           <div className="loading-orbit loading-orbit-secondary"></div>
  //           <div className="loading-core">
  //             <span className="loading-core-ring"></span>
  //             <span className="loading-core-dot"></span>
  //           </div>
  //         </div>
  //
  //         <div className="loading-copy">
  //           <p className="loading-kicker">Who This Actor</p>
  //           <h2 className="loading-text">Analisando a imagem...</h2>
  //           <p className="loading-subtext">
  //             Preparando os dados para encontrar a melhor correspondencia.
  //           </p>
  //         </div>
  //
  //         <div className="loading-progress" aria-hidden="true">
  //           <span></span>
  //           <span></span>
  //           <span></span>
  //         </div>
  //       </div>
  //     </section>
  //   );
};

export default Loading;
