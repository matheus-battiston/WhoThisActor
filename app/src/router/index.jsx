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
    path: "/exibirProducoes/:name",
    element: <ExibirProducoesScreen />,
  },
  {
    path: "/opcoesAtores",
    element: <PrivateRoute Screen={OpcoesAtoresScreen} />,
  },
  {
    path: "/exibirElenco/:name/:tipo",
    element: <ExibirElencoScreen />,
  },
]);
