import { IOffre } from 'app/shared/model/offre.model';

export interface IBadge {
  id?: number;
  label?: string | null;
  description?: string | null;
  offres?: IOffre[] | null;
}

export const defaultValue: Readonly<IBadge> = {};
