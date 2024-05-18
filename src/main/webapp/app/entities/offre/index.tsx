import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Offre from './offre';
import OffreDetail from './offre-detail';
import OffreUpdate from './offre-update';
import OffreDeleteDialog from './offre-delete-dialog';

const OffreRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Offre />} />
    <Route path="new" element={<OffreUpdate />} />
    <Route path=":id">
      <Route index element={<OffreDetail />} />
      <Route path="edit" element={<OffreUpdate />} />
      <Route path="delete" element={<OffreDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default OffreRoutes;
