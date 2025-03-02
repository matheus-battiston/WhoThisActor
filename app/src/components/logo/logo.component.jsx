import React from "react";
import logoImage from "../../assets/images/WhoThisActorLogo.png";
import "./logo.css"; // Importando o arquivo CSS

const Logo = () => {
  return (
    <div className="logo-container">
      <img
        src={logoImage} // Usando a importação correta da imagem
        alt="Logo"
        className="logo-image"
      />
    </div>
  );
};

export default Logo;
