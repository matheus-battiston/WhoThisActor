import "./tab.css";

const Tab = ({ setTab, estado, tabs, tamanho = "grande" }) => {
  const handleClick = (valor) => {
    setTab(valor);
  };

  return (
    <div className={` ${tamanho === "pequeno" ? "tabs-pequeno" : "tabs"}`}>
      {tabs.map((tab) => (
        <button
          key={tab}
          className={`${estado === tab ? "active" : ""} ${
            tamanho === "pequeno" ? "btn-pequeno" : "btn-grande"
          }`}
          onClick={() => handleClick(tab)}
        >
          {tab}
        </button>
      ))}
    </div>
  );
};

export default Tab;
