import CalendarMonthOutlinedIcon from "@mui/icons-material/CalendarMonthOutlined";
import CategoryOutlinedIcon from "@mui/icons-material/CategoryOutlined";
import TvOutlinedIcon from "@mui/icons-material/TvOutlined";

export default function ProducaoHeroMetadata({
  periodo,
  temporadas,
  genero,
  className,
}) {
  return (
    <div className={className}>
      {periodo ? (
        <span>
          <CalendarMonthOutlinedIcon />
          {periodo}
        </span>
      ) : null}
      {temporadas ? (
        <span>
          <TvOutlinedIcon />
          {temporadas}
        </span>
      ) : null}
      {genero ? (
        <span>
          <CategoryOutlinedIcon />
          {genero}
        </span>
      ) : null}
    </div>
  );
}
