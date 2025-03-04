import { useState } from "react";

export function useNomeAtor() {
  const [formInput, setFormInput] = useState("");

  function handleChange(event) {
    const { value } = event.target;
    setFormInput(value);
  }

  return {
    mudanca: handleChange,
    input: formInput,
  };
}
