import { IPays } from 'app/shared/model/pays.model';
import { IPlanning } from 'app/shared/model/planning.model';
import { IProgramme } from 'app/shared/model/programme.model';

export interface IPhoto {
  id?: number;
  url?: string | null;
  pays?: IPays | null;
  planning?: IPlanning | null;
  programme?: IProgramme | null;
}

export const defaultValue: Readonly<IPhoto> = {};
