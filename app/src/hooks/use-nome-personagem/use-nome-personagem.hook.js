import { useState } from "react";

export function useNomePersonagem() {
  const [formInput, setFormInput] = useState("");

  function handleChange(event) {
    const { value } = event.target;
    setFormInput(value);
  }

  return {
    changePersonagem: handleChange,
    personagemNome: formInput,
  };
}
