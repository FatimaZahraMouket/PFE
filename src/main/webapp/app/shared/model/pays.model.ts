import { IOffre } from 'app/shared/model/offre.model';
import { IPhoto } from 'app/shared/model/photo.model';

export interface IPays {
  id?: number;
  label?: string | null;
  description?: string | null;
  reviews?: number | null;
  continent?: string | null;
  latitude?: number | null;
  longitude?: number | null;
  isSaved?: boolean | null;
  isLiked?: boolean | null;
  offres?: IOffre[] | null;
  photos?: IPhoto[] | null;
}

export const defaultValue: Readonly<IPays> = {
  isSaved: false,
  isLiked: false,
};
