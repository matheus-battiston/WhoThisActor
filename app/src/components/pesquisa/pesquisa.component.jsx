import React from "react";
import "./pesquisa.css";

const InputSearch = ({ onChange, placeholder }) => {
  return (
    <div className="pesquisa">
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
