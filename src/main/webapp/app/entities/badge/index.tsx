import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Badge from './badge';
import BadgeDetail from './badge-detail';
import BadgeUpdate from './badge-update';
import BadgeDeleteDialog from './badge-delete-dialog';

const BadgeRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Badge />} />
    <Route path="new" element={<BadgeUpdate />} />
    <Route path=":id">
      <Route index element={<BadgeDetail />} />
      <Route path="edit" element={<BadgeUpdate />} />
      <Route path="delete" element={<BadgeDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BadgeRoutes;
