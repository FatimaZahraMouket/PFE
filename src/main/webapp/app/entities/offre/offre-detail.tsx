import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './offre.reducer';

export const OffreDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const offreEntity = useAppSelector(state => state.offre.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="offreDetailsHeading">
          <Translate contentKey="faeApp.offre.detail.title">Offre</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{offreEntity.id}</dd>
          <dt>
            <span id="label">
              <Translate contentKey="faeApp.offre.label">Label</Translate>
            </span>
          </dt>
          <dd>{offreEntity.label}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="faeApp.offre.description">Description</Translate>
            </span>
          </dt>
          <dd>{offreEntity.description}</dd>
          <dt>
            <span id="prix">
              <Translate contentKey="faeApp.offre.prix">Prix</Translate>
            </span>
          </dt>
          <dd>{offreEntity.prix}</dd>
          <dt>
            <span id="image">
              <Translate contentKey="faeApp.offre.image">Image</Translate>
            </span>
          </dt>
          <dd>{offreEntity.image}</dd>
          <dt>
            <span id="latitude">
              <Translate contentKey="faeApp.offre.latitude">Latitude</Translate>
            </span>
          </dt>
          <dd>{offreEntity.latitude}</dd>
          <dt>
            <span id="longitude">
              <Translate contentKey="faeApp.offre.longitude">Longitude</Translate>
            </span>
          </dt>
          <dd>{offreEntity.longitude}</dd>
          <dt>
            <span id="language">
              <Translate contentKey="faeApp.offre.language">Language</Translate>
            </span>
          </dt>
          <dd>{offreEntity.language}</dd>
          <dt>
            <Translate contentKey="faeApp.offre.themes">Themes</Translate>
          </dt>
          <dd>
            {offreEntity.themes
              ? offreEntity.themes.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {offreEntity.themes && i === offreEntity.themes.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="faeApp.offre.badges">Badges</Translate>
          </dt>
          <dd>
            {offreEntity.badges
              ? offreEntity.badges.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {offreEntity.badges && i === offreEntity.badges.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="faeApp.offre.pays">Pays</Translate>
          </dt>
          <dd>{offreEntity.pays ? offreEntity.pays.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/offre" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/offre/${offreEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OffreDetail;
