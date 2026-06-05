'use client';

import { DevsSolicitudResponse } from '../../types/historicoSolicitud.types';
import DavsSolicitudFrdBody from './DavsSolicitudFrdBody ';
import DavsSolicitudPseBody from './DavsSolicitudPseBody';

type Props = {
  data: DevsSolicitudResponse;
};

export default function DavsSolicitudBody({ data }: Props) {
  if (data.subtipo === 'PSE') {
    return <DavsSolicitudPseBody data={data} />;
  }

  return <DavsSolicitudFrdBody data={data} />;
}
