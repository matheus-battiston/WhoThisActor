import { useState } from "react";

export function useNomeSerie() {
  const [formInput, setFormInput] = useState("");

  function handleChange(event) {
    const { value } = event.target;
    setFormInput(value);
  }

  return {
    changeSerie: handleChange,
    serieNome: formInput,
  };
}
