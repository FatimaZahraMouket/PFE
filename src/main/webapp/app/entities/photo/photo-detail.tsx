import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './photo.reducer';

export const PhotoDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const photoEntity = useAppSelector(state => state.photo.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="photoDetailsHeading">
          <Translate contentKey="faeApp.photo.detail.title">Photo</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{photoEntity.id}</dd>
          <dt>
            <span id="url">
              <Translate contentKey="faeApp.photo.url">Url</Translate>
            </span>
          </dt>
          <dd>{photoEntity.url}</dd>
          <dt>
            <Translate contentKey="faeApp.photo.pays">Pays</Translate>
          </dt>
          <dd>{photoEntity.pays ? photoEntity.pays.id : ''}</dd>
          <dt>
            <Translate contentKey="faeApp.photo.planning">Planning</Translate>
          </dt>
          <dd>{photoEntity.planning ? photoEntity.planning.id : ''}</dd>
          <dt>
            <Translate contentKey="faeApp.photo.programme">Programme</Translate>
          </dt>
          <dd>{photoEntity.programme ? photoEntity.programme.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/photo" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/photo/${photoEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PhotoDetail;
