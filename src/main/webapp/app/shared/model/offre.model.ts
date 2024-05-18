import { IPlanning } from 'app/shared/model/planning.model';
import { ITheme } from 'app/shared/model/theme.model';
import { IBadge } from 'app/shared/model/badge.model';
import { IPays } from 'app/shared/model/pays.model';
import { Language } from 'app/shared/model/enumerations/language.model';

export interface IOffre {
  id?: number;
  label?: string | null;
  description?: string | null;
  prix?: number | null;
  image?: string | null;
  latitude?: number | null;
  longitude?: number | null;
  language?: Language | null;
  plannings?: IPlanning[] | null;
  themes?: ITheme[] | null;
  badges?: IBadge[] | null;
  pays?: IPays | null;
}

export const defaultValue: Readonly<IOffre> = {};
