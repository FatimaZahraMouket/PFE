import { IOffre } from 'app/shared/model/offre.model';

export interface ITheme {
  id?: number;
  label?: string | null;
  description?: string | null;
  offres?: IOffre[] | null;
}

export const defaultValue: Readonly<ITheme> = {};
