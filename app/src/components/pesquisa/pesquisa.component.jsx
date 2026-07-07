import React from "react";
import SearchIcon from "@mui/icons-material/Search";
import "./pesquisa.css";

const InputSearch = ({
  onChange,
  placeholder,
  wrapperClassName = "",
  inputClassName = "",
  mostrarIcone = false,
  ariaLabel,
  value,
  name,
  onKeyDown,
}) => {
  const classeWrapper = `pesquisa ${mostrarIcone ? "pesquisa-com-icone" : ""} ${wrapperClassName}`.trim();
  const classeInput = `input ${inputClassName}`.trim();

  return (
    <div className={classeWrapper}>
      {mostrarIcone ? (
        <SearchIcon className="pesquisa-icone" aria-hidden="true" />
      ) : null}
      <input
        className={classeInput}
        type="text"
        name={name}
        value={value}
        onChange={onChange}
        onKeyDown={onKeyDown}
        placeholder={placeholder}
        aria-label={ariaLabel || placeholder}
      />
    </div>
  );
};

export default InputSearch;
