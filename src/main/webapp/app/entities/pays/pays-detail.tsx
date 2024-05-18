import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './pays.reducer';

export const PaysDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const paysEntity = useAppSelector(state => state.pays.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="paysDetailsHeading">
          <Translate contentKey="faeApp.pays.detail.title">Pays</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{paysEntity.id}</dd>
          <dt>
            <span id="label">
              <Translate contentKey="faeApp.pays.label">Label</Translate>
            </span>
          </dt>
          <dd>{paysEntity.label}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="faeApp.pays.description">Description</Translate>
            </span>
          </dt>
          <dd>{paysEntity.description}</dd>
          <dt>
            <span id="reviews">
              <Translate contentKey="faeApp.pays.reviews">Reviews</Translate>
            </span>
          </dt>
          <dd>{paysEntity.reviews}</dd>
          <dt>
            <span id="continent">
              <Translate contentKey="faeApp.pays.continent">Continent</Translate>
            </span>
          </dt>
          <dd>{paysEntity.continent}</dd>
          <dt>
            <span id="latitude">
              <Translate contentKey="faeApp.pays.latitude">Latitude</Translate>
            </span>
          </dt>
          <dd>{paysEntity.latitude}</dd>
          <dt>
            <span id="longitude">
              <Translate contentKey="faeApp.pays.longitude">Longitude</Translate>
            </span>
          </dt>
          <dd>{paysEntity.longitude}</dd>
          <dt>
            <span id="isSaved">
              <Translate contentKey="faeApp.pays.isSaved">Is Saved</Translate>
            </span>
          </dt>
          <dd>{paysEntity.isSaved ? 'true' : 'false'}</dd>
          <dt>
            <span id="isLiked">
              <Translate contentKey="faeApp.pays.isLiked">Is Liked</Translate>
            </span>
          </dt>
          <dd>{paysEntity.isLiked ? 'true' : 'false'}</dd>
        </dl>
        <Button tag={Link} to="/pays" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/pays/${paysEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PaysDetail;
