import { useState } from "react";

export function useNomeAtor() {
  const [formInput, setFormInput] = useState(""); // Agora formInputs é uma string

  function handleChange(event) {
    const { value } = event.target;
    setFormInput(value); // Agora armazena o valor como string
  }

  return {
    mudanca: handleChange,
    input: formInput, // dadosDosInputs é agora uma string
  };
}
