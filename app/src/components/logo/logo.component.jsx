import React from "react";
import "./logo.css";

const Logo = ({ className = "" }) => {
  return (
    <img
      src={`${process.env.PUBLIC_URL}/logo/WhoThisActorLogo.png`}
      alt="Logo"
      className={`logo-image ${className}`.trim()}
      width="300"
      height="300"
      loading="eager"
      fetchPriority="high"
    />
  );
};

export default Logo;
