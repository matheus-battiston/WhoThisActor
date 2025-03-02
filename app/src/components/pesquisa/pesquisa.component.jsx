import React from "react";
import "./pesquisa.css"; // Importando o arquivo de estilo

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
