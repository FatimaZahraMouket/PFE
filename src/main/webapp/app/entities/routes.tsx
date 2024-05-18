import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Pays from './pays';
import Theme from './theme';
import Badge from './badge';
import Offre from './offre';
import Planning from './planning';
import Programme from './programme';
import Photo from './photo';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="pays/*" element={<Pays />} />
        <Route path="theme/*" element={<Theme />} />
        <Route path="badge/*" element={<Badge />} />
        <Route path="offre/*" element={<Offre />} />
        <Route path="planning/*" element={<Planning />} />
        <Route path="programme/*" element={<Programme />} />
        <Route path="photo/*" element={<Photo />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
