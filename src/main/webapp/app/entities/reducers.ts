import pays from 'app/entities/pays/pays.reducer';
import theme from 'app/entities/theme/theme.reducer';
import badge from 'app/entities/badge/badge.reducer';
import offre from 'app/entities/offre/offre.reducer';
import planning from 'app/entities/planning/planning.reducer';
import programme from 'app/entities/programme/programme.reducer';
import photo from 'app/entities/photo/photo.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  pays,
  theme,
  badge,
  offre,
  planning,
  programme,
  photo,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
