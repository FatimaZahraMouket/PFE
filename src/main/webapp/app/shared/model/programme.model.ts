import { IPhoto } from 'app/shared/model/photo.model';
import { IPlanning } from 'app/shared/model/planning.model';

export interface IProgramme {
  id?: number;
  heure?: string | null;
  description?: string | null;
  photos?: IPhoto[] | null;
  planning?: IPlanning | null;
}

export const defaultValue: Readonly<IProgramme> = {};
