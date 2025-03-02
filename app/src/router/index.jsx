import { createBrowserRouter } from "react-router-dom";
import { PrivateRoute } from "./private-router.component";
import {
  HomeScreen,
  ExibirProducoesScreen,
  OpcoesAtoresScreen,
  ExibirElencoScreen,
} from "../screens/index";

export const router = createBrowserRouter([
  {
    path: "/",
    element: <HomeScreen />,
  },
  {
    path: "/exibirProducoes/:name", // Rota com o parâmetro dinâmico
    element: <ExibirProducoesScreen />,
  },
  {
    path: "/opcoesAtores", // Rota com o parâmetro dinâmico
    element: <PrivateRoute Screen={OpcoesAtoresScreen} />,
  },
  {
    path: "/exibirElenco/:name/:tipo", // Rota com o parâmetro dinâmico
    element: <ExibirElencoScreen />,
  },
]);
