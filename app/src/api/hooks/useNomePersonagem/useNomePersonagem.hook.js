import { useState } from "react";

export function useNomePersonagem() {
  const [formInput, setFormInput] = useState(""); // Agora formInputs é uma string

  function handleChange(event) {
    const { value } = event.target;
    setFormInput(value); // Agora armazena o valor como string
  }

  return {
    changePersonagem: handleChange,
    personagemNome: formInput, // dadosDosInputs é agora uma string
  };
}
