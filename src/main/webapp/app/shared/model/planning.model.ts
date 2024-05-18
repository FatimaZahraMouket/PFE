import { IProgramme } from 'app/shared/model/programme.model';
import { IPhoto } from 'app/shared/model/photo.model';
import { IOffre } from 'app/shared/model/offre.model';

export interface IPlanning {
  id?: number;
  titre?: string | null;
  description?: string | null;
  jourNumero?: number | null;
  programmes?: IProgramme[] | null;
  photos?: IPhoto[] | null;
  offre?: IOffre | null;
}

export const defaultValue: Readonly<IPlanning> = {};
