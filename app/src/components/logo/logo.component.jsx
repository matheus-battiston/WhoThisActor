import React from "react";
import logoImage from "../../assets/images/WhoThisActorLogo.png";
import "./logo.css";

const Logo = () => {
  return (
    <div className="logo-container">
      <img src={logoImage} alt="Logo" className="logo-image" />
    </div>
  );
};

export default Logo;
