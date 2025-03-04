import React from "react";
import "./pesquisa.css";

const InputSearch = ({ onChange, texto, placeholder }) => {
  return (
    <div className="pesquisa">
      <p className="titlePesquisa">{texto}</p>
      <input
        className="input"
        type="text"
        onChange={onChange}
        placeholder={placeholder}
      />
    </div>
  );
};

export default InputSearch;
